const format = require('../format_check');

const {pool} = require('../secret_info/db_connect');

//센서 수정 
module.exports.sensor_modification = (req,res)=>{
    let wifi_mac_address = req.body.wifi_mac_address; //맥주소
    let board_nickname = req.body.board_nickname; //닉네임
    let address = req.body.address; //주소
    let phone_number = req.body.phone_number;// 연락처
    let latitude = req.body.latitude; //센서 위도 
    let longitude = req.body.longitude; //센서 경도 
    if(!format.checkValidMacAddress(wifi_mac_address))
    {
        res.send({'key':1}) //센서 맥주소 양식 에러 
        return;
    }

    if(!format.phone_check(phone_number))
    {
      res.send({'key':2}) //폰번호양식에러
      return;
    }
    pool.getConnection().then((conn)=>{
        conn.query(`update Sensor set board_nickname='${board_nickname}', phone_number='${phone_number}', address='${address}',latitude ='${latitude}',longitude='${longitude}'
        where wifi_mac_address='${wifi_mac_address}'`).then((data)=>{
            console.log('센서정보수정성공')
            res.send({'key':3})//센서정보 수정 성공 
        }).catch((err)=>{
            console.log('센서정보 수정시 에러발생1')
            console.log(err)
            res.send({'key':0,'err_code':err.code});
        })
        conn.release();
    }).catch((err)=>{
        console.log('센서정보 수정시 에러발생2')
        res.send({'key':0,'err_code':err.code});
    })
}