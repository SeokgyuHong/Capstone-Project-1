//사용하지않는 파일 

//낙상처리 알람 모듈 
//const mysql = require('mysql');
//const config = require('./db_config.json');
const fcm = require('./send_fcm');

//const pool = mysql.createPool(config);
const {pool} = require('../secret_info/db_connect');
//낙상 알림 발생시 처리해주는 모듈 
function insert_alarm_table(mac_addr, alarm_time, res){
    pool.getConnection().then((conn)=>{
      let insert_query ='insert into Alarm(wifi_mac_address, alamr_time) values (?, ?)';
      conn.query(insert_query,mac_addr,alarm_time).then((data)=>{
        res.send({'key':2}) // 데이터에 alarm 등록
      }).catch((err)=>{
        console.log('데이터베이스에 낙상 알림 추가 시 에러 발생',+err.code);
        res.send({'key':-1,'err_code':err.code});
      })
      conn.release();
    }).catch((err)=>{
      console.log('데이터베이스 낙상 알림 처리시 에러 발생',+err.code);
      res.send({'key':-1,'err_code':err.code});
    })
}

// 낙상 알람 처리 모듈 (db에 넣고 fcm보내기)
module.exports.fall_alarm_process = function(req,res){
    let mac_address = req.body.mac_address; // post로 mac address 받아오기
    let alarm_time = req.body.alarm_time; // post로 alarm time 받아오기

    insert_alarm_table(mac_address, alarm_time, res); // alarm data table에 넣어주기
    fcm.send_fcm_byMac(mac_address, res); // fcm 보내기
    fcm.send_fcm(mac_address,res);
}