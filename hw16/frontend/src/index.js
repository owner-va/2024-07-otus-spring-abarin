import React from 'react';
import ReactDOM from 'react-dom';
import Books from './components/Books';

const App = () => (
  <div>
    <Books />
  </div>
);

ReactDOM.render(<App />, document.getElementById('root'));
