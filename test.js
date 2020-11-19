var mysql = require('mysql');

var connection = mysql.createConnection({
  host:'jongp.cxnxil3u3tnt.us-east-1.rds.amazonaws.com',
  post:'3306',
  user:'jongp2',
  password:'1234',
  database:'jongpdb',
  dateStrings:'date'
});

function checkExistMacAddress(addr, cb){
    let check_query = 'select wifi_mac_address from Sensor where Sensor.wifi_mac_address=?';
    connection.query(check_query, addr, function(error, results){
        if(error){
          console.log(error);
          res.status(200).send({'key':error.code});
        }
        else{
            console.log('query 있음');
            if(results[0]==undefined){ // 해당 mac address 값이 없을 때
                console.log("There is no id");
                cb(false);
            }
            else{
                console.log('있음');
                cb(true);
            }
        }
    })
}

checkExistMacAddress('72:9D:F3:8B:2B:71', (result)=> {
    console.log(result);
});

