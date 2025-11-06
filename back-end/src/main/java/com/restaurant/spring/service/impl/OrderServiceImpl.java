package com.restaurant.spring.service.impl;

import com.restaurant.spring.controller.vm.RequestOrderVm;
import com.restaurant.spring.controller.vm.ResponseOrderVm;
import com.restaurant.spring.controller.vm.UserOrderResponseVm;
import com.restaurant.spring.dto.ContactInfoDto;
import com.restaurant.spring.dto.OrderDto;
import com.restaurant.spring.dto.ProductDto;
import com.restaurant.spring.dto.security.AccountDto;
import com.restaurant.spring.mapper.OrderMapper;
import com.restaurant.spring.mapper.ProductMapper;
import com.restaurant.spring.mapper.security.AccountMapper;
import com.restaurant.spring.model.ContactInfo;
import com.restaurant.spring.model.Order;
import com.restaurant.spring.model.Product;
import com.restaurant.spring.model.security.Account;
import com.restaurant.spring.repo.OrderRepo;
import com.restaurant.spring.repo.ProductRepo;
import com.restaurant.spring.service.NotificationService;
import com.restaurant.spring.service.OrderService;
import com.restaurant.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    public static final String CODE = "CODE-";


    private AccountMapper accountMapper;
    private OrderMapper orderMapper;
    private OrderRepo orderRepo;
    private ProductRepo productRepo;
    private NotificationService notificationService;

    public OrderServiceImpl(AccountMapper accountMapper,
                            OrderMapper orderMapper ,
                            OrderRepo orderRepo,
                            ProductRepo productRepo,
                            NotificationService notificationService) {
        this.accountMapper = accountMapper;
        this.orderMapper = orderMapper;
        this.orderRepo = orderRepo;
        this.productRepo = productRepo;
        this.notificationService = notificationService;
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "orders", allEntries = true),
            @CacheEvict(value = "userOrders", allEntries = true),
            @CacheEvict(value = "searchOrders", allEntries = true)
    })
    public ResponseOrderVm requestOrder(RequestOrderVm requestOrderVm) {
        List<Product> products = productRepo.findAllById(requestOrderVm.getProductsIds());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDto accountDto = (AccountDto)authentication.getPrincipal();
        if (accountDto.getAccountDetails() == null) {
            throw new RuntimeException("account.details.not.found");
        }
        Order order = new Order();
        order.setTotalNumber(requestOrderVm.getTotalNumber());
        order.setTotalPrice(requestOrderVm.getTotalPrice());
        order.setCode(CODE);
        order.setAccount(accountMapper.toAccount(accountDto));
        order.setProducts(products);
        Order savedOrder = orderRepo.save(order);
        savedOrder.setCode(CODE + savedOrder.getId());
        savedOrder = orderRepo.save(savedOrder);

        //handle order notification
        Account currentAccount = accountMapper.toAccount(accountDto);
        String notificationType = "NEW_ORDER";
        String message = String.format(
                "Your order %s has been placed successfully on %s with a total of $%.2f for %.0f item(s).",
                savedOrder.getCode(),
                savedOrder.getOrderDate().toLocalDate(),
                savedOrder.getTotalPrice(),
                savedOrder.getTotalNumber()
        );
        notificationService.handleNotification(currentAccount, currentAccount, message, notificationType);
        //handle order notification End

        return new ResponseOrderVm(savedOrder.getCode(), savedOrder.getTotalPrice() ,  savedOrder.getTotalNumber());

    }

    @Override
    @Cacheable(
            value = "userOrders",
            key = "'user_' + T(org.springframework.security.core.context.SecurityContextHolder).context.authentication.principal.username + '_page_' + #page + '_size_' + #size"
    )
    public UserOrderResponseVm getUserOrders(int page,int size) {
        Pageable pageable = getPageable(page, size);
        AccountDto accountDto = (AccountDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Order> orders  = orderRepo.findByAccountIdOrderByIdDesc(accountDto.getId(),pageable);
        if (orders.isEmpty()) {
            throw new RuntimeException("order.not.found");
        }
        List<OrderDto> orderDtos = orderMapper.toOrderDtoList(orders.getContent());
        double totalPrice = orderDtos.stream()
                .mapToDouble(OrderDto::getTotalPrice).sum();
        Integer totalSize = (int) orderDtos.stream()
                .mapToDouble(OrderDto::getTotalNumber).sum();

        return  new UserOrderResponseVm(orderDtos, totalSize, totalPrice,orders.getTotalElements());
    }

    @Override
    @Cacheable(
            value = "orders",
            key = "'page_' + #page + '_size_' + #size + '_username_' + (#username == null ? 'all' : #username)"
    )
    public UserOrderResponseVm getAllOrders(int page,int size,String username) {
        Pageable pageable = getPageable(page, size);
        Page<Order> orders;
        if (username != null && !username.trim().isEmpty()) {
          orders = orderRepo.findByUsername(username, pageable);
        }else{
            orders = orderRepo.findAllByOrderByIdDesc(pageable);
        }
        if (orders.isEmpty()) {
            throw new RuntimeException("order.not.found");
        }

        List<OrderDto> orderDtos = orderMapper.toOrderDtoList(orders.getContent());

        double totalPrice = orderDtos.stream()
                .mapToDouble(OrderDto::getTotalPrice)
                .sum();

        int totalSize = (int) orderDtos.stream()
                .mapToDouble(OrderDto::getTotalNumber)
                .sum();

        return new UserOrderResponseVm(orderDtos, totalSize, totalPrice,orders.getTotalElements());
    }

    @Override
    @Cacheable(value = "orderById", key = "#id")
    public OrderDto getOrderById(Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("order.not.found"));
        return orderMapper.toOrderDto(order);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "orders", allEntries = true),
            @CacheEvict(value = "userOrders", allEntries = true),
            @CacheEvict(value = "searchOrders", allEntries = true),
            @CacheEvict(value = "orderById", key = "#id")
    })
    public void deleteOrder(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("order.not.found"));
        orderRepo.delete(order);
    }

    private static Pageable getPageable(int page, int size) {
        try {
            if (page < 1) {
                throw new RuntimeException("error.min.one.page");
            }
            return PageRequest.of(page - 1, size);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Cacheable(
            value = "searchOrders",
            key = "'keyword_' + #keyword + '_page_' + #page + '_size_' + #size"
    )
    public UserOrderResponseVm searchUserOrders(String keyword,int page,int size) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getUserOrders(page,size);
        }
        AccountDto accountDto = (AccountDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Order> orders = orderRepo.searchUserOrdersByKeyword(accountDto.getId(),keyword.trim());

        if (orders.isEmpty()) {
            throw new RuntimeException("order.not.found");
        }

        long total = orders.size();
        int totalPages = (int) Math.ceil((double) total / size);
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int start = (page - 1) * size;
        int end = Math.min(start + size, orders.size());
        if (start >= orders.size()) {
            throw new RuntimeException("contacts.not.found");
        }

        List<Order> paginatedList = orders.subList(start, end);

        List<OrderDto> orderDtos = orderMapper.toOrderDtoList(paginatedList);

        return new UserOrderResponseVm(orderDtos,(long) orders.size());

    }
}
