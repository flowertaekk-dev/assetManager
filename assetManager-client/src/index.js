import React from 'react';
import ReactDOM from 'react-dom';
import * as serviceWorker from './serviceWorker';
import { BrowserRouter, Route, Redirect } from 'react-router-dom'

import App from './App/App';
import Header from './Header/Header'
import Login from './Login/Login'

import './index.css';

const Index = () => {
  return (
    <BrowserRouter>
      <Header />

      <Route exact path="/" component={App} />
      <Route path="/login" component={Login}/>
      <Redirect path="*" to="/" />
    </BrowserRouter>
  )
}

ReactDOM.render(
  <Index />,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
