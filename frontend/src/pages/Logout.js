import Page from '../components/Page'
import Header from '../components/Header'
import Navbar from '../components/Navbar'
import Button from '../components/Button'
import Main from '../components/Main'
import { Username } from './Username'

export default function Logout() {
  return (
    <Page>
      <Header title="Logout" />
      <Main>
        <p>
          You are logged in as <Username>Jane Doe</Username>
        </p>
        <Button>Log out</Button>
      </Main>
      <Navbar />
    </Page>
  )
}
