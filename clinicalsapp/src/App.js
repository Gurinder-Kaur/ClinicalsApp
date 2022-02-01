import React from 'react'
import './App.css';
import Home from './components/Home'
import AddPatient from './components/AddPatient'
import AnalyzeData from './components/AnalyzeData'
import CollectClinicals from './components/CollectClinicals'
import ChartGenerator from './components/ChartGenerator'
import {Route,Routes} from 'react-router-dom';
import {BrowserRouter} from 'react-router-dom'

function App() {
  return (
    <BrowserRouter>
    <div className="App">
      <h2>THE CLINICAL APP</h2>
      
      <Routes>
        <Route exact path="/" element={<Home/>}></Route>
        <Route exact path="/patientDetails/:patientId" element={<CollectClinicals/>}></Route>
        <Route exact path="/addPatient" element={<AddPatient/>}></Route>
        <Route exact path="/analyze/:patientId" element={<AnalyzeData/>}></Route>
        <Route exact path="/chart/:componentName/:patientId" element={<ChartGenerator/>}></Route>
      </Routes>
    </div>
    </BrowserRouter>
  );
}

export default App;
