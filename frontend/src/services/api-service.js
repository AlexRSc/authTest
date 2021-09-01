import axios from 'axios'

export const getToken = credentials =>
  axios
    .post('api/rem213/auth/access_token', credentials)
    .then(response => response.data)
    .then(dto => dto.token)
