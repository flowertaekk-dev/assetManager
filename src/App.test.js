import React from 'react';
import { render } from '@testing-library/react';
import App from './App';

test('App main screen', () => {
  const { getByText } = render(<App />);
  const linkElement = getByText(/Hello AssetManager/i);
  expect(linkElement).toBeInTheDocument();
});
