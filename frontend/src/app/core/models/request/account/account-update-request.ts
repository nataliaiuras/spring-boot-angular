import {AccountType} from '../../enum/account-type';
import {CurrencyType} from '../../enum/currency-type';

export interface AccountUpdateRequest {
  type: AccountType;
  currency: CurrencyType;
  customerId: number;
}
