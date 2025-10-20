export class ChangePassReq {
  username?: string;
  oldPassword?: string;
  newPassword: string;
  confirmPassword: string;
}
