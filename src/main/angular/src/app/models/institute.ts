import {Branch} from './branch';

export interface Institute {
  id: number;
  bankCode: string;
  name: string;
  website: string;
  branches: Branch[];
  createdDate: string;
  lastModifiedDate: string;
  version: number;
}
