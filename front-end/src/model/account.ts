import {AccountDetails} from './account-details';
import {Role} from './role';

export class Account {
  id: number;
  username: string;
  enabled: boolean;
  roles: Role[];
  accountDetails: AccountDetails;
}
