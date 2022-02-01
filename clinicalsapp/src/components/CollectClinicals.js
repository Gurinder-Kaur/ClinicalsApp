
import React , {Component} from 'react';
import axios from 'axios';
import {Link} from "react-router-dom"
import {toast} from 'react-toastify'
import { useParams } from 'react-router-dom';

function withParams(Component) {
    return props => <Component {...props} params={useParams()} />;
  }
class CollectClinicals extends React.Component{

    
    state={}
    
    componentWillMount(){
        let {patientId} = this.props.params
        axios.get("http://localhost:8080/clinicalservices/api/patients/"+patientId).then(res=>{
            this.setState(res.data)
        })
    }

    handleSubmit(event){
        event.preventDefault();
        let {patientId} = this.props.params
        const data={
            patientId: patientId,
            componentName: this.componentName,
            componentValue: this.componentValue
        }
        axios.post("http://localhost:8080/clinicalservices/api/clinicals/",data)
        .then(res=>{
            toast("Patient data saved successfully!",{autoClose:2000, position:toast.POSITION.BOTTOM_CENTER})
        })

    }
    render(){ 
        let {patientId} = this.props.params       
        return(<div> 
            
            <h2>Patient Details: </h2>
            Patient Id: {patientId} <br/>
            First Name: {this.state.firstName}<br/>
            Last Name: {this.state.lastName}<br/>
            Age: {this.state.age}<br/>
            <h2>Clinical Data:</h2>
            <form>
                Clinical Entry Type <select onChange={(event)=>{this.componentName=event.target.value}}>
                    <option>Select one</option>
                    <option value="bp">Blood Pressure(sys/dys)</option>
                    <option value="hw">Height/Weight</option>
                    <option value="heartRate">Heart Rate</option>
                </select>
                <br/>
                <br/>
                Value: <input type="text" name="ComponentValue" onChange={(event)=>{this.componentValue=event.target.value}}></input>
                <br/>
                <br/>
                <button onClick={this.handleSubmit.bind(this)}>Confirm</button>
            </form>
            <Link to="/">Go Back</Link>
        </div>)
    }
}

export default withParams(CollectClinicals);