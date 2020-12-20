const format = require('../local_lib/format_check');

const {pool} = require('../secret_info/db_connect');


//센서리스트 조회함수 
module.exports.sensor_list_request = (req,res)=>{
    let email_address = req.body.email_address;//이메일주소

    let data_array = new Array();

    if(!format.e_mail_check(email_address))
    {
      res.send({'key':-1}) //이메일 양식 에러
      return;
    }

    pool.getConnection().then((conn)=>{
        conn.query(`select wifi_mac_address, board_nickname,phone_number,address,sensor_status from Sensor where email_address ='${email_address}'`).then((data)=>{
            for(let i=0; i<data.length; i++)
            {
                data_array.push(data[i]);
            }
            res.send({'key':1,'list':data_array}); //센서리스트 전달 성공
        }).catch((err)=>{
            console.log('센서리스트 가져오는중 에러발생')
            res.send({'key':0,'err_code':err.code}); 
        })
        conn.release();
    }).catch((err)=>{
        console.log('센서리스트 가져오는중 에러발생2')
        res.send({'key':0,'err_code':err.code}); 
    })
}