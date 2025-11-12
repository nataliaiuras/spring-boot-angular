import {AccountResponse} from './account-response';
import {CardResponse} from '../card/card-response';
import {CustomerResponse} from '../customer/customer-response';

export interface AccountDetailResponse extends AccountResponse {
  customer: CustomerResponse;
  cards: CardResponse[];
  createdDate: string;
  version: number;
}
