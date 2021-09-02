import Page from '../components/Page'
import Header from '../components/Header'
import Navbar from '../components/Navbar'
import Button from '../components/Button'
import Select from '../components/Select'
import Main from '../components/Main'
import UserPassword from '../components/UserPassword'
import { useAuth } from '../auth/AuthProvider'

export default function Admin() {
  const { user } = useAuth()
  return (
    <Page>
      <Header title="Admin" />
      <Main>
        <Select title="Select a user" values={['one', 'two', 'three']} />
        <Button>Reset Password</Button>
        <Button>Delete User</Button>
      </Main>
      <UserPassword user="Janedoe" password="12345" />
      <Navbar user={user} />
    </Page>
  )
}
