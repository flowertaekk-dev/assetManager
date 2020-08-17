import React, { useEffect, useState } from 'react'

import {GOOGLE_API_KEY, GOOGLE_CLIENT_ID} from '../../config'

import './Header.css'

const Header = props => {

    // GOOGLE DRIVE API
    const [name, setName] = useState('')
    const [googleAuth, setGoogleAuth] = useState('')

    const SCOPE = 'https://www.googleapis.com/auth/drive'
    const DISCOVERY_DOCS = ['https://www.googleapis.com/discovery/v1/apis/drive/v3/rest']
    const API_KEY = GOOGLE_API_KEY
    const CLIENT_ID = GOOGLE_CLIENT_ID

    useEffect(() => {
        let script = document.createElement('script')
        script.onload = () => {window.gapi.load('client:auth2', initClient)}
        script.src="https://apis.google.com/js/api.js";
        document.body.appendChild(script);
    }, [])

    const initClient = () => {
        try {
            window.gapi.client.init({
                'apiKey': API_KEY,
                'clientId': CLIENT_ID,
                'discoveryDocs': DISCOVERY_DOCS,
                'scope': SCOPE
              }).then(() => {
                setGoogleAuth(window.gapi.auth2.getAuthInstance())
                console.log('access_token', window.gapi.auth2)
                // googleAuth.isSignedIn.listen(updateSigninStatus)
              })
        } catch (e) {
            console.log(e) // TODO gt 에러처리!
        }
    }

    const signInFunction =()=>{
        googleAuth.signIn()
        updateSigninStatus()
    }
    
    const signOutFunction =()=>{
        // googleAuth.signOut()
        // updateSigninStatus()


        window.gapi.client.drive.files.create({
            name: 'assetManager.sql',
            mimeType: 'application/json'
        }).then(res => {
            console.log(res.status)
        })

    }
    
    const updateSigninStatus = ()=> {
        setSigninStatus()
    }

    const setSigninStatus = async () => {
        let user = googleAuth.currentUser.get()
        console.log('user', user)

        if (user.wc == null){
            // User를 못 찾음
            // TODO gt 에러 처리 필요?
            setName('')
        } else {

            let isAuthorized = user.hasGrantedScopes(SCOPE)
            console.log('isAuthorized', isAuthorized)

            if (isAuthorized) {
                window.gapi.client.drive.files.list({
                    'pageSize': 10,
                        'fields': "nextPageToken, files(id, name)"
                }).then((response) => {
                    console.log('api response', response.result)
                })
            }
        }
    }
    // /GOOGLE DRIVE API

    return (
        <header className="Header">
            <p className="Header__login__text" onClick={signInFunction}>Log in</p>
            <p className="Header__login__text" onClick={signInFunction}>Sign in</p>
            <p className="Header__login__text" onClick={signOutFunction}>Sign out</p>
        </header>
    )
}

export default Header