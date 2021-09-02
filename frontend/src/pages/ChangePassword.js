import Page from '../components/Page'
import Header from '../components/Header'
import Navbar from '../components/Navbar'
import Button from '../components/Button'
import TextField from '../components/TextField'
import ButtonGroup from '../components/ButtonGroup'
import Main from '../components/Main'
import { useAuth } from '../auth/AuthProvider'

export default function ChangePassword() {
  const { user } = useAuth()
  return (
    <Page>
      <Header title="Change Password" />
      <Main as="form">
        <TextField title="New Password" name="new-password" type="password" />
        <TextField
          title="Retype Password"
          name="retype-password"
          type="password"
        />
        <ButtonGroup>
          <Button secondary>Cancel</Button>
          <Button>Save</Button>
        </ButtonGroup>
      </Main>
      <Navbar user={user} />
    </Page>
  )
}
