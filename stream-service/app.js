import express from 'express';
import mysql from 'mysql2';
import http from 'http';
import fs from 'fs';
import bodyParser from 'body-parser';
import XMLHttpRequest from 'xhr2';
import session from 'express-session';

const app = express();
app.use(session({secret:'secret'}));
var ssn;

app.listen(8085, () => console.log('listening 8085'));
app.use(express.static('public'));

app.get('/getvideosdata', (req, res) => {
  const db = mysql.createConnection({
    host      : 'sql-service',
    user      : 'root',
    password  : '123456',
    database  : 'videoDB'
  });

  db.connect((err) => {
    if(err){
      throw err;
    }
    console.log('mysql connected');
  })

  let sql = 'SELECT * FROM videos'
  db.query(sql, (err, result) => {
    if(err){
      throw err;
    } 
    ssn = req.session;

    ssn.videosData = result;
    res.send(result);
  });
});

app.get('/videos/:name', (req, res) =>{

  const vidname = req.params['name'];
  const file = fs.createWriteStream('./public/videos/' + vidname + ".mp4");
  //get pathURL of clicked video from session variable
  ssn = req.session;
  const videosData = ssn.videosData;
  var desiredURL;
  for(let vid of videosData){
    if(vid["video_name"] == vidname)
      desiredURL = vid["pathURL"];
  }

  // make get request to videos storage microservice using pathURL
  // then save result in local videos folder
  const request = http.get(desiredURL, function(response) {
   response.pipe(file);
   // after download completed close filestream
   file.on("finish", () => {
       file.close();
       console.log("video Download Completed");
   });
  });
  res.end(); 
});

app.post('/auth', bodyParser.urlencoded({ extended: false }), async (req, res) => {
    const username = req.body['username']; 
    const password = req.body['password'];

    var sender = new XMLHttpRequest();
    sender.open("POST", "http://auth-service:8082/authenticate/", true);
    sender.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    sender.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
        console.log(this.responseText);
         if(this.responseText === 'true')
            res.redirect("http://" + req.headers.host + "/stream.html");
          else{
            res.redirect("http://" + req.headers.host + "/index.html");
          }
      }
    };
    sender.send("username=" + username + "&password=" + password);
});