import styled from 'styled-components/macro'

import Page from '../components/Page'
import Header from '../components/Header'
import Navbar from '../components/Navbar'
import Button from '../components/Button'
import { Link } from 'react-router-dom'
import Main from '../components/Main'
import Avatar from '../components/Avatar'
import Badge from '../components/Badge'

export default function Profile() {
  return (
    <Page>
      <Header title="Jane Doe" />
      <Main>
        <Avatar src="https://thispersondoesnotexist.com/image" alt="" />
        <Badge>user</Badge>
        <Button as={LinkStyled} to="/change-password">
          Change Password
        </Button>
      </Main>
      <Navbar />
    </Page>
  )
}

const LinkStyled = styled(Link)`
  text-decoration: none;
`
