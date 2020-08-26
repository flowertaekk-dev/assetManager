import React, { useEffect } from 'react';

import customAxios from '../customAxios'


import './App.css';

const App = (props) => {


  useEffect(() => {
    customAxios('/healthcheck', (data) => {
      console.log('healthcheck', data)
    })
  }, [])

  return (
    <section className="App">
      <h1 className="first__greeting">Hello AssetManager</h1>
    </section>
  );
}

export default App;
