import axios from 'axios'

export const getToken = credentials =>
  axios
    .post('api/rem213/auth/access_token', credentials)
    .then(response => response.data)
    .then(dto => dto.token)

export const setNewPassword = (token, password) =>
    axios.create({
        headers:{Authorization:`Bearer ${token}`}})
        .put("api/rem213/user/password", {password})
        .then(response=>response.data)
