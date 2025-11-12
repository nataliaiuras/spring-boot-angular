
export interface ApiResponse<T> {
  data?: T;
  success: boolean;
  message?: string;
  errorCode?: string;
  timestamp: string;
}
