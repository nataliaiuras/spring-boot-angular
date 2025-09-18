import {Institute} from './institute';
import {Address} from './address';
import {Client} from './client';

export interface Branch {
  id: number;
  branchCode: string;
  locationCode: string;
  bicCode: string;
  name: string;
  email: string;
  phoneNumber: string;
  address: Address;
  institute: Institute;
  clients: Client[];
}
