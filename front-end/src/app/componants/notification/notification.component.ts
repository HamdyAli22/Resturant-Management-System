import {Component, OnInit, Output, EventEmitter} from '@angular/core';
import {NotificationService} from '../../../service/notification.service';
import {Notification} from '../../../model/notification';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {

  notifications: Notification[] = [];
  username: string = sessionStorage.getItem('userName') || '';
  loading = false;
  @Output() closed = new EventEmitter<void>();
  @Output() unreadCountChanged = new EventEmitter<number>();
  activeTab: 'all' | 'unread' = 'all';
  unreadCount = 0;

  constructor(private notificationService: NotificationService) { }

  ngOnInit(): void {
    if (this.username) {
      this.loadNotifications();
    }
  }

  loadNotifications(): void {
    this.loading = true;
    this.notificationService.getAllByUser(this.username).subscribe({
      next: (data) => {
        this.notifications = data.sort((a, b) => (b.id ?? 0) - (a.id ?? 0));
        this.updateUnreadCount();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.notifications = [];
      }
    });
  }

  filteredNotifications(): Notification[] {
    return this.activeTab === 'unread'
      ? this.notifications.filter(n => !n.read)
      : this.notifications;
  }

  markAsRead(notification: Notification): void {
    if (!notification.read) {
      notification.read = true;
      this.notificationService.markAsRead(notification.id).subscribe({
        next: () => {
          this.updateUnreadCount();
        },
        error: () => {
          notification.read = false;
          this.updateUnreadCount();
        }
      });
    }
  }

  updateUnreadCount(): void {
    this.unreadCount = this.notifications.filter(n => !n.read).length;
    this.unreadCountChanged.emit(this.unreadCount);
  }
  setActiveTab(tab: 'all' | 'unread'): void {
    this.activeTab = tab;
    // if (tab === 'unread') {
    //   this.notifications.forEach(n => n.read = true);
    //   this.updateUnreadCount(); // هيخلي البادج تختفي تلقائيًا
    // }
  }

  getTimeAgo(dateStr: string): string {
    const now = new Date();
    const past = new Date(dateStr);
    const diffMs = now.getTime() - past.getTime();

    const seconds = Math.floor(diffMs / 1000);
    const minutes = Math.floor(seconds / 60);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);

    if (days > 0) { return `${days}d`; }
    if (hours > 0) { return `${hours}h`; }
    if (minutes > 0) { return `${minutes}m`; }
    return `${seconds}s`;
  }


  closeDropdown(): void {
    this.closed.emit();
  }
}
