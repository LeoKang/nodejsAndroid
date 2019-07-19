const express = require('express');
const app = express();

let users = [
  {    id: 1,    pw: 'alice'  },
  {    id: 2,    pw: 'bek'  },
  {    id: 3,    pw: 'chris'  }
]

app.get('/users', (req, res) => {
   console.log('who get in here/users');
   res.json(users)
});

app.post('/post', (req, res) => {
   console.log('who get in here post /users');
   var inputData;

   req.on('data', (data) => {
     inputData = JSON.parse(data);
   });

   req.on('end', () => {
     console.log("id : "+inputData.id + " , password : "+inputData.pw);
   });

   res.write("Connect success!");
   res.end();
});

app.listen(3000, () => {
  console.log('Example app listening on port 3000!');
});
