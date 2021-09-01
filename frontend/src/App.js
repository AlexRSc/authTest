import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import Login from './pages/Login'
import Profile from './pages/Profile'
import ChangePassword from './pages/ChangePassword'
import Logout from './pages/Logout'
import Admin from './pages/Admin'
import Welcome from './pages/Welcome'

export default function App() {
  return (
    <Router>
      <Switch>
        <Route exact path="/" component={Welcome} />
        <Route exact path="/profile" component={Profile} />
        <Route path="/login" component={Login} />
        <Route path="/logout" component={Logout} />
        <Route path="/admin" component={Admin} />
        <Route path="/change-password" component={ChangePassword} />
      </Switch>
    </Router>
  )
}
