import React, { useState, useEffect } from 'react';

import customAxios from '../../customAxios'


import './App.css';

const App = (props) => {

  const [ip, setIp] = useState('')

  useEffect(() => {
    customAxios('/ip', (data) => {
      setIp(data)
    })
  }, [])

  return (
    <section className="App">
      <h1 className="first__greeting">Hello AssetManager</h1>
      <h2>{ip}</h2>
    </section>
  );
}

export default App;
