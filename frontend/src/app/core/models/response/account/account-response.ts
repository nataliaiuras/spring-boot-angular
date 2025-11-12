import {CurrencyType} from '../../enum/currency-type';
import {AccountType} from '../../enum/account-type';

export interface AccountResponse {
  id: Number;
  accountNumber: string;
  ibanCode: string;
  type: AccountType;
  currency: CurrencyType;
  balance: number;
  active: boolean;
  lastModifiedDate: string;
}
