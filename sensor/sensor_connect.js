
const format = require('../format_check');
const request = require('request');
const {pool} = require('../secret_info/db_connect');
const db_loginfo = require('../secret_info/db_loginfo');
//센서 중복 체크 (센서 등록시)

const convert = require('xml-js'); //xml -json 변환

module.exports.sensor_duplication_check = (req,res)=>{
  let wifi_mac_address = req.body.wifi_mac_address; //맥주소 전달 받음
  
  if(!format.checkValidMacAddress(wifi_mac_address))
  {
      res.send({'key':1}) //센서 양식 에러
      return;
  }
  pool.getConnection().then((conn)=>{
    conn.query(`select * from Sensor where wifi_mac_address='${wifi_mac_address}'`).then((data)=>{
      if(data[0]===undefined)
      {
        res.send({'key':2}) //센서 중복 없음 
      }
      else
      {
        res.send({'key':3})//센서 중복 있음
      }
    }).catch((err)=>{
      console.log('센서중복체크시 에러1'+err.code);
      res.send({'key':0,'err_code':err.code});
    })
    conn.release();
  }).catch((err)=>{
    console.log('센서중복체크시 에러2'+err.code);
    res.send({'key':0,'err_code':err.code});
  })
}

function call_api(wifi_mac_address,latitude,longitude){
  let ServiceKey = db_loginfo.api_key();
  let url = `http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncLcinfoInqire?WGS84_LON=${longitude}&WGS84_LAT=${latitude}&pageNo=1&numOfROws=5&${ServiceKey}`;
  //console.log(url)
  return new Promise((resolve,reject)=>{
   
    request(url,(err,res,body)=>{
      //let xml_to_json = convert.xml2json(body,{compact:true,spaces:1});
      let xml_to_json = convert.xml2json(body,{compact:true,spaces:1});
      xml_to_json = JSON.parse(xml_to_json);
      //console.log(xml_to_json["elements"][0]["elements"][1])
      let length = xml_to_json["response"]["body"]["items"]["item"].length
      //console.log(xml_to_json["response"]["body"]["items"]["item"].length)
      for(let i=0; i<length; i++)
      {
          let hospital_name = xml_to_json["response"]["body"]["items"]["item"][i].dutyName._text;
          let hospital_location = xml_to_json["response"]["body"]["items"]["item"][i].dutyAddr._text;
          let hospital_phone_num =xml_to_json["response"]["body"]["items"]["item"][i].dutyTel1._text;
          let hospital_latitude = xml_to_json["response"]["body"]["items"]["item"][i].latitude._text;
          let hospital_longitude = xml_to_json["response"]["body"]["items"]["item"][i].longitude._text;
          console.log(hospital_name,hospital_location,hospital_phone_num,hospital_latitude,hospital_longitude);
          pool.getConnection().then((conn)=>{
              conn.query(`insert into Hospital (hospital_name,hospital_location,hospital_phone_num,latitude,longitude,wifi_mac_address)
              values('${hospital_name}','${hospital_location}','${hospital_phone_num}','${hospital_latitude}','${hospital_longitude}','${wifi_mac_address}')`).then((data)=>{
                  console.log('병원 정보 db 삽입 성공 !')
                  resolve('병원 정보 삽입 성공 성공!');
              }).catch((err)=>{
                  console.log('병원정보 db 삽입중 에러 2'+err)
                  reject('병원 정보 db 삽입중 에러 2'+err);
              })
          conn.release();
          }).catch((err)=>{
              console.log('병원정보 db에 삽입중 에러'+err)
              reject('병원 정보 db 삽입중 에러'+err);
          })
        
        }
      if(err)
      {
        reject('api호출시에러'+err);
      }
    })
  })
}

module.exports.sensor_registration = (req,res)=>{
  let wifi_mac_address = req.body.wifi_mac_address; //센서 맥주소
  let email_address = req.body.email_address; //사용자 이메일 주소
  let board_nickname = req.body.board_nickname;  //센서 닉네임
  let phone_number = req.body.phone_number; //센서사용자 연락처
  let address = req.body.address; //센서 사용자 주소
  let latitude = req.body.latitude; //센서 위도 
  let longitude = req.body.longitude; //센서 경도 
  if(!format.checkValidMacAddress(wifi_mac_address))
  {
      res.send({'key':1}) //센서 양식 에러
      return;
  }
  if(!format.e_mail_check(email_address))
  {
    res.send({'key':-1}) //이메일 양식 에러
    return;
  }
  if(!format.phone_check(phone_number))
  {
    res.send({'key':-2}) //폰번호양식에러
    return;
  }
  pool.getConnection().then((conn)=>{
    conn.query(`insert into Sensor (wifi_mac_address, board_nickname, phone_number, address, sensor_status, email_address,latitude,longitude)
    values ('${wifi_mac_address}', '${board_nickname}', '${phone_number}', '${address}', '1', '${email_address}','${latitude}','${longitude}')`).then((data)=>{
      console.log('센서등록성공')
      call_api(wifi_mac_address,latitude,longitude).then((resolve)=>{
        console.log(resolve);
        res.send({'key':2}) //센서 등롱 성공 및 병원 정보 삽입 성공
      }).catch((reject)=>{
        console.log(reject)
        res.send({'key':3}) //센서등롱 성공했으나 병원정보 삽입 실패 
      });
      //res.send({'key':2})//센서등록성공

    }).catch((err)=>{
      console.log('센서 등록시 에러 발생1 '+err);
      res.send({'key':0,'err_code':err.code});
    })
    conn.release();
  }).catch((err)=>{
    console.log('센서 등록시 에러 발생2 '+err.code);
    res.send({'key':0,'err_code':err.code});
  })
  
}
// 
// function sensor_connect(mac_addr, info, res){
//     pool.getConnection(function(error, connection){
//         let update_query = 'update Sensor set email_address=?, phone_number=?, board_nickname=?, address=?,sensor_status=? where wifi_mac_address=?';  // sensor table에 update
//         connection.query(update_query, [info["email_address"], info["phone_number"], info["board_nickname"],info["address"], '1', mac_addr], function(error,result){
//           if(error){
//             console.log(error);
//             res.status(200).send({'key':error.code});
//           }
//           else{ // 성공적으로 sensor가 연결됨
//               res.send({'key':1});
//           }
//         })
//         connection.release();
//     })
// }

// // sensor가 연결이 안되어 있는지 확인한 후 연결시키는 모듈
// module.exports.check_and_sensor_connect =  function(req,res){
//     let mac_addr = req.body.m_addr; // post로 mac address 받아오기
//     let info = req.body.info; // post로 email, location, board_nickname 받아오기

//     if(format.checkValidMacAddress(mac_addr)){ // 올바른 mac address인가
//       pool.getConnection(function(err, connection){
//         let check_query = 'select sensor_status from Sensor where Sensor.wifi_mac_address=?';
//         connection.query(check_query, mac_addr, function(error, results){
//             if(error){
//               console.log(error);
//               res.status(200).send({'key':-3, 'err_code':error.code});
//               connection.release();
//             }
//             else{
//                 if(results[0]==undefined){ // 해당 sensor가 없을 때
//                   res.send({'key':-1}); // connect 불가능! (sensor 등록이 안됐음)
//                 }
//                 else if(results[0].sensor_status=='0'){
//                     console.log('0입니다');
//                     sensor_connect(mac_addr, info, res);
//                 }
//                 else{
//                     console.log('1입니다');
//                     res.send({'key':-2}); // connect 불가능! (이미 connect 되어있음)
//                 }
//             }
//         })
//       })
//     }
//     else{
//       res.send({'key':0}); // connect 불가능! (mac address 형식 맞지 않음)
//     }
// }