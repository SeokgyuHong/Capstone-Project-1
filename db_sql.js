const {pool} = require('./secret_info/db_connect');
const { resolveContent } = require('nodemailer/lib/shared');

//social 네이버 계정 로그인시 저장하는 함수 
module.exports.sql_insert_naver= (req,res)=>{ //네이버 로그인 계정 db에 저장
    let email = req.body.email;
    let social = 'naver';
    pool.getConnection().then((conn)=>
    {
        conn.query(`select * from user_social where email_address ='${email}'`).then((data)=>{ //해당 이메일이 Db에 저장되어있는지 확인
            if(data[0]===undefined) //저장된 계정이 없을때
            { //아래 쿼리를 통해 네이버 로그인 정보 db상에 저장
                conn.query(`insert into user_social(email_address,social,sign_in_state)values('${email}','${social}',1);`).then((data)=>{
                    console.log('저장완료');
                    res.send("저장완료");
                }).catch((err)=>
                    {
                    console.log(err);
                    res.send(err);
                    });
            }
            else
            { //이미 저장된 계정이 있을경우 sign_in_state만 1로 바꿔줌 
                conn.query(`update user_social set sign_in_state=1 where email_address='${email}';`).then((data)=>{
                    console.log('업데이트완료');
                    res.send("업데이트완료");
                }).catch((err)=>{
                    console.log(err);
                    res.send(err);
                    });
            }
        conn.release(); //query connection pool에서 할당 받은 conn객체 다시 release
    }).catch((err)=>{
            if(err) 
            {
                console.log(err);
                res.send(err);
            }
        });
    });
}
//social 카카오 계정 로그인 시 저장하는 함수
module.exports.sql_insert_kakao= (req,res)=>{
    let email = req.body.email;
    let social = 'kakao';
    pool.getConnection().then((conn)=>{
        conn.query(`select * from user_social where email_address ='${email}'`).then((data)=>{ //해당 이메일이 Db에 저장되어있는지 확인
            if(data[0]===undefined) //저장된 계정이 없을때
            { //아래 쿼리를 통해 카카오 로그인 정보 db상에 저장
                conn.query(`insert into user_social(email_address,social,sign_in_state)values('${email}','${social}',1);`).then((data)=>{
                    console.log('저장완료');
                    res.send("저장완료");
                }).catch((err)=>{
                    console.log(err);
                    res.send(err);
                    });
            }
            else
            { //이미 저장된 계정이 있을경우 sign_in_state만 1로 바꿔줌 
                conn.query(`update user_social set sign_in_state=1 where email_address='${email}';`).then((data)=>{
                    console.log('업데이트완료');
                    res.send("업데이트완료");
                }).catch((err)=>{
                        console.log(err);
                        res.send(err);
                    });
            }
        conn.release(); //query connection pool에서 할당 받은 conn객체 다시 release
        }).catch((err)=>{
                if(err) 
                {   
                    console.log(err)
                    res.send(err);
                };
            });
    });
}
//클라이언트에서 알람 일별, 월별 데이터 갯수 요청시 제공해주는 api
module.exports.sql_alarm_count =(req,res)=>{ //일별 월별 알람 갯수 전송
    let date = req.body.date;
    let return_val;
    let lite ="";
    pool.getConnection().then((conn)=>{
        if(date.length==6)
        {
            lite = "%Y%m";
        }
        else if(date.length==8) 
        { 
            lite="%Y%m%d"; //일별 혹은 월별 알람 갯수 요청을 위한 쿼리 구분
        }
        conn.query(`select count(*) from alarm where date_format(alarm_time,'${lite}')='${date}'`).then((data)=>{ //일별 월별 알람 데이터 갯수 요청쿼리
            return_val = data[0]['count(*)'];
            console.log("return:"+return_val);
            res.send(""+return_val); //요청갯수 client에게 전송
        }).catch((err)=>{
                console.log(err);
                res.send(err);
            })
        conn.release();
    }).catch((err)=>{
            if(err) 
            {   
                console.log(err);
                res.send(err);
            }
        })
}
//클라이언트에서 알람 데이터 정보 일별, 월별 요청시 제공해주는 api
module.exports.sql_alarm_data_request = (req,res)=>{
    let date = req.body.date;
    let data_array= new Array();
    let lite="";
    pool.getConnection().then((conn)=>{
        if(date.length==8) //일별조회
        {
            lite = "%Y%m%d";
        }
        else if(date.length==6)
        {
            lite = "%Y%m";
        }//월별조회 
        //일별 월별 구분하여 알람 정보를 view 테이블 생성 및 조회를 통해 참조
        conn.query(`create view alarm_data_view as select alarm_time, wifi_mac_address from alarm where date_format(alarm_time, '${lite}')='${date}';`).then((data)=>{
            conn.query(`select * from alarm_data_view`).then((data)=>{
                conn.query('drop view alarm_data_view');
                for(let i=0; i<data.length; i++)//일별 혹은 월별 알람 데이터 배열 형식으로 저장 
                {
                    data_array.push(data[i]);
                }
                res.send(data_array); //저장된 json배열을 클라이언트에게 전달
            })
        }).catch((err)=>{
                console.log(err);
                res.send(err);
            })
        conn.release(); //database connection pool에서 가져온 객체 release
    }).catch((err)=>{
            console.log(err);
            res.send(err);
        })  
    
}
