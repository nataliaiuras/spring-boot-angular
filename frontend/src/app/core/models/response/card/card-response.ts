import { AccountResponse } from '../account/account-response';

export interface CardResponse {
  id: number;
  cardNumber: string;
  cardHolder: string;
  validThru: string;
  cvvCode: number;
  pin: number;
  account: AccountResponse;
  createdDate: string;
  lastModifiedDate: string;
  version: number;
}
