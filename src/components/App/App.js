import React, { useEffect } from 'react';

import {fire, fireDB} from '../../firebase'

import './App.css';

const App = (props) => {

  useEffect(() => {
    fire()
  }, [])

  const test = () => {
    fireDB()
      .then(res => {
        console.log(res.val())
      })
  }

  return (
    <section className="App">
      <h1 className="first__greeting" onClick={test}>Hello AssetManager</h1>
    </section>
  );
}

export default App;
