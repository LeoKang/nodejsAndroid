const express = require('express');
var oracledb = require('oracledb');
const app = express();
var msg = "init msg";

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

     // --- oracledb START ----------------------------
     oracledb.getConnection({
       user : inputData.id,
       password : inputData.pw,
       connectionString : "localhost/XE"
     }, function(err, connection) {
       if(err) {
         console.error(err.message);
         return;
       }
       console.log('==> userlist search query');

       var query = 'SELECT * FROM dept';
       connection.execute(query, function(err, result) {
         if(err) {
           console.error(err.message);
           msg = err;
           return;
         }
         console.log(result.rows);
         console.log(result);
         console.log(result.rows[0]);
         msg = result;
       });
     });
     // --- oracledb END ----------------------------
   });

   res.write(msg);
   res.end();
});

app.listen(3000, () => {
  console.log('Example app listening on port 3000!');
});
