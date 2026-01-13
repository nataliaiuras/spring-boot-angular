import {AccountType} from '../../enum/account-type';
import {CurrencyType} from '../../enum/currency-type';

export interface AccountCreateRequest {
  type: AccountType;
  currency: CurrencyType;
  customerId: number;
}
