
const format = require('../format_check');
const {pool} = require('../secret_info/db_connect');





module.exports.sensor_on = (req,res)=>{

    let wifi_mac_address = req.body.wifi_mac_address//와이파이 맥주소

    if(!format.checkValidMacAddress(wifi_mac_address))
    {
        res.send({'key':1}) //센서 양식 에러
        return;
    }

    pool.getConnection().then((conn)=>{
        conn.query(`select sensor_status from Sensor where wifi_mac_address ='${wifi_mac_address}'`).then((data)=>{
            if(data[0]['sensor_status']=='0') //센서가 꺼져있으면 
            {
                conn.query(`update Sensor set sensor_status='1' where wifi_mac_address='${wifi_mac_address}'`).then((data)=>{
                    res.send({'key':2}) // 센서 on
                }).catch((err)=>{
                    console.log('센서 키는 중 에러 발생1 ',err.code);
                    res.send({'key':0,'err_code':err.code}); 
                })
            }
            else
            {
                res.send({'key':3})//센서가 이미 켜져있음 (에러)
            }
        
        }).catch((err)=>{
            console.log('센서 키는 중 에러 발생2 ',err.code);
            res.send({'key':0,'err_code':err.code}); 
        })
        conn.release();
    }).catch((err)=>{
        console.log('센서 키는 중 에러 발생3 ',err.code);
        res.send({'key':0,'err_code':err.code}); 
    })
}

module.exports.sensor_off = (req,res)=>{

    let wifi_mac_address = req.body.wifi_mac_address//와이파이 맥주소

    if(!format.checkValidMacAddress(wifi_mac_address))
    {
        res.send({'key':1}) //센서 양식 에러
        return;
    }

    pool.getConnection().then((conn)=>{
        conn.query(`select sensor_status from Sensor where wifi_mac_address ='${wifi_mac_address}'`).then((data)=>{
            if(data[0]['sensor_status']=='1') //센서가 켜져있으면 
            {
                conn.query(`update Sensor set sensor_status='0' where wifi_mac_address='${wifi_mac_address}'`).then((data)=>{
                    res.send({'key':2}) // 센서 off
                }).catch((err)=>{
                    console.log('센서 끄는 중 에러 발생1 ',err.code);
                    res.send({'key':0,'err_code':err.code}); 
                })
            }
            else
            {
                res.send({'key':3})//센서가 이미 꺼져있음 (에러)
            }
        
        }).catch((err)=>{
            console.log('센서 끄는 중 에러 발생2 ',err.code);
            res.send({'key':0,'err_code':err.code}); 
        })
        conn.release();
    }).catch((err)=>{
        console.log('센서 끄는 중 에러 발생3 ',err.code);
        res.send({'key':0,'err_code':err.code}); 
    })
}