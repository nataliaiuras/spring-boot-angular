import {AddressResponse} from './address-response';

export interface AddressDetailResponse extends AddressResponse {

  addressLine2: string;
  createdDate: string;
  lastModifiedDate: string;
  version: number;

}
