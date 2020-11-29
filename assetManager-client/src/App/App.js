import React, { useEffect } from 'react';
import { Redirect } from 'react-router-dom'

import customAxios from '../customAxios'
import useStore from '../mobx/useStore'

import './App.css';

const App = (props) => {

  const { loginUser } = useStore()

  useEffect(() => {
    customAxios('/healthcheck', (data) => {
      console.log('healthcheck', data)
    })
  }, [])

  return (
    <section className="App">
      { loginUser.loginUser.id && <Redirect to={'/tableMap'} /> }
      <h1 className="first__greeting">Hello AssetManager</h1>
    </section>
  );
}

export default App;
