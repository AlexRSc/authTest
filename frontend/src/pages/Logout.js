import Page from '../components/Page'
import Header from '../components/Header'
import Navbar from '../components/Navbar'
import Button from '../components/Button'
import Main from '../components/Main'
import Username from '../components/Username'
import { useAuth } from '../auth/AuthProvider'

export default function Logout() {
  const { user, logout } = useAuth()
  return (
    <Page>
      <Header title="Logout" />
      <Main>
        <p>
          You are logged in as <Username>{user.username}</Username>
        </p>
        <Button onClick={logout}>Log out</Button>
      </Main>
      <Navbar user={user} />
    </Page>
  )
}
