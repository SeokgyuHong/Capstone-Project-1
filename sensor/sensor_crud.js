//const mysql = require('mysql');
//const config = require('./db_config.json');
const format = require('../format_check');

//const pool = mysql.createPool(config);

const {pool} = require('../secret_info/db_connect');
// mac address 등록 함수
function sensor_register(m_addr, res){
  let mac_addr = m_addr;

  pool.getConnection(function(error, connection){
    let insert_query = 'insert into Sensor(wifi_mac_address, sensor_status) values (?, 0)';  // sensor table에 mac address 새로 등록
    connection.query(insert_query, mac_addr,function(error,result){
      if(error){
        console.log(error);
        res.status(200).send({'key':-2, 'err_code':error.code});
      }
      else{ // 성공적으로 sensor가 sensor테이블에 등록됨
          res.send({'key':1});
      }
    })
    connection.release();
  })
};

// mac address 삭제 함수
function sensor_deletion(mac_addr, res){
  pool.getConnection(function(error, connection){
    let delete_query = 'delete from Sensor where wifi_mac_address=?';  // sensor table에 mac address 삭제
    connection.query(delete_query, mac_addr,function(error,result){
      if(error){
        console.log(error);
        res.status(200).send({'key':-2, 'err_code':error.code});
      }
      else{ // 성공적으로 sensor가 sensor테이블에서 삭제됨
          res.send({'key':1});
      }
    })
    connection.release();
  })
};

// 해당 mac address sensor location 변경
function sensor_modification(m_addr, res, modi){
  let mac_addr = m_addr;

  pool.getConnection(function(error, connection){
    let update_query = 'update from Sensor set location=? where wifi_mac_address=?';  // sensor table에 new location update
    connection.query(update_query, modi["new_location"], mac_addr, function(error,result){
      if(error){
        console.log(error);
        res.status(200).send({'key':-2, 'err_code':error.code});
      }
      else{ // 성공적으로 new location이 update됨
          res.send({'key':1});
      }
    })
    connection.release();
  })
};

// 확인 후 sensor 등록, 삭제, 수정하는 모듈
module.exports.check_and_sensor_crud =  function(req,res,flag){
  let mac_addr = req.body.m_addr; // post로 mac address 받아오기
  // let flag = req.body.flag; // post로 delete할건지, register할건지, modify할건지 받아오기
  let modi = req.body.modify;
    console.log(mac_addr);
    console.log(modi);
  if(format.checkValidMacAddress(mac_addr)){
      console.log(pool)
    pool.getConnection(function(err, connection){
      let check_query = 'select wifi_mac_address from Sensor where Sensor.wifi_mac_address=?';
    console.log(check_query);
      connection.query(check_query, mac_addr, function(error, results){
          console.log('쿼리결과',results);
          if(error){
            console.log(error);
            res.status(200).send({'key':error.code});
            connection.release();
          }
          else{
              if(results[0]==undefined){ // 해당 mac address 값이 없을 때
                console.log("없음");
                if(flag === 'regi'){
                  sensor_register(mac_addr, res); // 등록 가능
                }
                else{
                  res.send({'key':-1}) // 삭제, 수정 불가능! (기존에 없음)
                }
                connection.release();
              }
              else{ 
                  console.log('있음');
                  if(flag === 'modi'){ // 수정 가능
                    sensor_modification(mac_addr, res, modi);
                  }
                  else if(flag === 'del'){ // 삭제 가능
                    sensor_deletion(mac_addr,res);
                  }
                  else{
                    res.send({'key':-1}) // 등록 불가능! (기존에 있음)
                  }
                  connection.release();
              }
          }
      })
    })
  }
  else{
    console.log('형식이 맞지 않습니다.');
    res.send({'key':0}); // 등록 불가능! (형식 맞지 않음)
  }
}

// 센서 리스트 출력
module.exports.check_sensor_list =  function(req,res){
  let email_addr = req.body.e_addr; // post로 email address 받아오기

  pool.getConnection(function(err, connection){
    let print_query = 'select board_nickname, location, wifi_mac_address from Sensor where Sensor.email_address=?';
    connection.query(print_query, email_addr, function(error, results){
        if(error){
          console.log(error);
          res.status(200).send({'key':-2, 'err_code':error.code});
          connection.release();
        }
        else{
            if(results[0]==undefined){ // 등록한 센서가 없을 때
              console.log("센서 없음");
              res.send({'key':-1}) // 등록한 센서가 없음
              connection.release();
            }
            else{
              console.log('센서 있음');
              res.send(results); // 등록된 센서 json 넘겨줌
              connection.release();
            }
        }
    })
  })
}