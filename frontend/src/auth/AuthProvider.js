import AuthContext from './AuthContext'
import { useContext, useState } from 'react'
import jwt from 'jsonwebtoken'
import {getToken, setNewPassword} from '../services/api-service'

export default function AuthProvider({ children }) {
  const [token, setToken] = useState()

  const claims = jwt.decode(token)

  const user = claims && {
    username: claims.sub,
    avatar: claims.avatar,
    role: claims.role,
  }

  const login = credentials => getToken(credentials).then(setToken)

  const logout = () => setToken()

  const changePassword = (password) => setNewPassword(token, password).then(console.log)

  return (
    <AuthContext.Provider value={{ token, user, login, logout, changePassword }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
