//낙상처리 알람 모듈 
//const mysql = require('mysql');
//const config = require('./db_config.json');
const fcm = require('../fcm/send_fcm');

//const pool = mysql.createPool(config);
const {pool} = require('../secret_info/db_connect');
//낙상 알림 발생시 처리해주는 모듈 
function insert_alarm_table(mac_addr, alarm_time, res){
    pool.getConnection(function(error, connection){
        let insert_query = 'insert into Alarm(wifi_mac_address, alamr_time) values (?, ?)';  // alarm table에 insert
        connection.query(insert_query, mac_addr, alarm_time, function(error,result){
          if(error){
            console.log(error);
            res.send({'key':-3, 'err_code':error.code});
          }
          else{ // 성공적으로 alarm이 alarm table에 등록됨!
              res.send({'key':2});
          }
        })
        connection.release();
    })
}

// 낙상 알람 처리 모듈 (db에 넣고 fcm보내기)
module.exports.fall_alarm_process = function(req,res){
    let mac_address = req.body.mac_address; // post로 mac address 받아오기
    let alarm_time = req.body.alarm_time; // post로 alarm time 받아오기

    insert_alarm_table(mac_addr, mac_address, res); // alarm data table에 넣어주기
    fcm.send_fcm_byMac(mac_address, res); // fcm 보내기
    fcm.send_fcm(mac_address,res);
}