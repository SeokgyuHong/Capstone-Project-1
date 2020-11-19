const mysql = require('mysql');
const config = require('./db_config.json');
const format = require('./format_check.js');

const pool = mysql.createPool(config);

// 
function sensor_connect(mac_addr, info, res){
    pool.getConnection(function(error, connection){
        let update_query = 'update Sensor set email_address=?, location=?, board_nickname=?, sensor_status=? where wifi_mac_address=?';  // sensor table에 update
        connection.query(update_query, [info["e_addr"], info["location"], info["nickname"], '1', mac_addr], function(error,result){
          if(error){
            console.log(error);
            res.status(200).send({'key':error.code});
          }
          else{ // 성공적으로 sensor가 연결됨
              res.send({'key':1});
          }
        })
        connection.release();
    })
}

// sensor가 연결이 안되어 있는지 확인한 후 연결시키는 모듈
module.exports.check_and_sensor_connect =  function(req,res){
    let mac_addr = req.body.m_addr; // post로 mac address 받아오기
    let info = req.body.info; // post로 email, location, board_nickname 받아오기

    if(format.checkValidMacAddress(mac_addr)){ // 올바른 mac address인가
      pool.getConnection(function(err, connection){
        let check_query = 'select sensor_status from Sensor where Sensor.wifi_mac_address=?';
        connection.query(check_query, mac_addr, function(error, results){
            if(error){
              console.log(error);
              res.status(200).send({'key':-3, 'err_code':error.code});
              connection.release();
            }
            else{
                if(results[0]==undefined){ // 해당 sensor가 없을 때
                  res.send({'key':-1}); // connect 불가능! (sensor 등록이 안됐음)
                }
                else if(results[0].sensor_status=='0'){
                    console.log('0입니다');
                    sensor_connect(mac_addr, info, res);
                }
                else{
                    console.log('1입니다');
                    res.send({'key':-2}); // connect 불가능! (이미 connect 되어있음)
                }
            }
        })
      })
    }
    else{
      res.send({'key':0}); // connect 불가능! (mac address 형식 맞지 않음)
    }
}