import Button from './Button'
import styled from 'styled-components/macro'
import { Username } from '../pages/Username'

export default function UserPassword({ user, password, ...props }) {
  return (
    <Wrapper {...props}>
      <p>
        New password set for <Username>{user}</Username>
      </p>
      <Button secondary>Copy</Button>
    </Wrapper>
  )
}

const Wrapper = styled.section`
  border-top: 1px solid var(--neutral-dark);
  padding: var(--size-m);
  width: 100%;
  display: grid;
  grid-column-gap: var(--size-xl);
  grid-template-columns: 1fr min-content;
  align-items: center;
`
