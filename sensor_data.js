const { TIMEOUT } = require('dns');
const { fchmod } = require('fs');
const mysql = require('mysql');
const config = require('./db_config.json');
const format = require('./format_check.js');

const pool = mysql.createPool(config);


// sensor edge board에서 받은 데이터를 디비에 넣어주는 모듈
module.exports.measurement_data_tansmission =  function(req,res){
    let mac_addr = req.body.m_addr; // post로 mac address 받아오기
    let ms_time = req.body.ms_time; // post로 measurement_time 받아오기
    let ms_data = req.body.ms_data; // post로 measurement_data 받아오기
    // flag = true;
    pool.getConnection(function(err, connection){
        let insert_query = 'insert into Data (wifi_mac_address, measurement_time, measurement_data) values (?, ?, ?)';
        connection.query(insert_query, [mac_addr, ms_time, ms_data], function(error, results){
            if(error){
                console.log(error);
                res.status(200).send({'key':error.code});
                connection.release();
            }
            else{
                res.send({'key':1}); // sensor Edge board에게 insert 성공 알려주기
            }
        })
    })
}


// // 이제부터는 session 사용 가정 하!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// function connect_ing(body){
//     return 1;
// }


// // sensor edge board에서 받은 데이터를 디비에 넣어주는 모듈
// module.exports.measurement_data_tansmission =  function(req,res){
//     let mac_addr = req.body.m_addr; // post로 mac address 받아오기
//     let ms_time = req.body.ms_time; // post로 measurement_time 받아오기
//     let ms_data = req.body.ms_data; // post로 measurement_data 받아오기

//     if(mac_addr!=undefined){ // 데이터를 전달해주는 mac_addr가 있다면
//         connect_ing(req.session.body); //session을 사용한다는 가정하에... ***************************
//     }
//     // flag = true;
//     pool.getConnection(function(err, connection){
//         let insert_query = 'insert into Data (wifi_mac_address, measurement_time, measurement_data) values (?, ?, ?)';
//         connection.query(insert_query, [mac_addr, ms_time, ms_data], function(error, results){
//             if(error){
//                 console.log(error);
//                 res.status(200).send({'key':error.code});
//                 connection.release();
//             }
//             else{
//                 res.send({'key':1}); // sensor Edge board에게 insert 성공 알려주기
//             }
//         })
//     })
// }

// module.exports.check_connect = function(body, res){
//     var flag = true;
//     while (flag){
//         flag = false;
//         TIMEOUT(3); // 3초 후 flag가 false인 채로 continue;
//         flag = connect_ing(body);
//     }
// }

// // var flag = true;
// // while (flag){
// //     flag = false;
// //     TIMEOUT(3);
// // }
// // fcm()