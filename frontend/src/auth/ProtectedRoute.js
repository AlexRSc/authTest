import { Redirect, Route } from 'react-router-dom'

export default function ProtectedRoute({ adminOnly, user, ...props }) {
  if (!user) {
    return <Redirect to="/login" />
  }

  if (adminOnly && user.role !== 'admin') {
    return <Redirect to="/" />
  }

  return <Route {...props} />
}
