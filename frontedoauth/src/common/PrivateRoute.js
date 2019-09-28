import React from 'react';
import {
    Route,
    Redirect
  } from "react-router-dom";
  
  
const PrivateRoute = ({ component: Component, authenticated, currentLoginPhase, ...rest }) => (
    <Route
      {...rest}
      render={props =>
        authenticated && currentLoginPhase === 2 ? (
          <Component {...rest} {...props} />
        ) : (
          <Redirect
            to={{
              pathname: '/login',
              state: { from: props.location }
            }}
          />
        )
      }
    />
);
  
export default PrivateRoute