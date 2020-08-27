import React from 'react';
import { render } from '@testing-library/react';
import Login from './Login';

test('Login button is shown', () => {
  const { getByText } = render(<Login />);

  const sign_in = getByText(/SIGN IN/i);
  expect(sign_in).toBeInTheDocument();

  const cancel = getByText(/CANCEL/i);
  expect(cancel).toBeInTheDocument();
});
