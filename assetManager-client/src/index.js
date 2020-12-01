import React from 'react';
import ReactDOM from 'react-dom';
import * as serviceWorker from './serviceWorker';
import { BrowserRouter, Redirect, Route } from 'react-router-dom'

import useStore from './mobx/useStore'

import App from './App/App';
import Header from './Header/Header'
import Login from './Login/Login'
import Signup from './Login/Signup/Signup'
import TableMap from './TableMap/TableMap'
import Setting from './Setting/Setting'
import UpdateUserData from './Login/UpdateUserData/UpdateUserData'

import './index.css';

const Index = () => {

  const { loginUser } = useStore()

  return (
    <BrowserRouter>
      <Header />

      {/* 로그인 중이 아니면 무조건 home 으로 리다이렉트 */}
      { !loginUser.loginUser && <Redirect path="*" to="/" /> }

      <Route exact path="/" component={App} />
      <Route path="/login" component={Login}/>
      <Route path="/signup" component={Signup}/>
      <Route path="/tableMap" component={TableMap} />
      <Route path="/setting" component={Setting} />
      <Route path="/updateUserData" component={UpdateUserData} />



      {/* 개발할 때는 주석처리 */}
      {/* <Redirect path="*" to="/" /> */}
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
