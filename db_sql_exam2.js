// 10월 28일 과제 
const {pool} = require('./db_connect');
function e_mail_check(e_mail){
    let e_mail_check = /^[0-9a-zA-z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;     
    if(!e_mail_check.test(e_mail))
    {
        return false;
    }
    else
    {
        return true;
    }
}
function phone_check(phone_number){
    let phone_number_check_1 = /^[0-9]{3}[-]+[0-9]{4}[-]+[0-9]{4}$/;     //휴대전화
    let phone_number_check_2 = /^\d{2,3}-\d{3,4}-\d{4}$/;  //일반전화 
    if(!phone_number_check_1.test(phone_number)&&!phone_number_check_2.test(phone_number)) //일반전화번호와 휴대폰번호 둘다 양식에서 어긋날경우
    {
        return false;
    }
    else
    {
        return true;
    }

}
function company_check(company_number){
    let company_number_check = /^\d{3}-\d{2}-\d{3}$/;
    if(!company_number_check.test(company_number))
    {
        return false;
    }
    else
    {
        return true;
    }
}

//기관정보 요청 함수 
module.exports.inst_req = (req,res)=>{
    let id = req.body.id; //조회하고자 하는 기관 아이디
    
    pool.getConnection().then((conn)=>{
        conn.query(`select * from institution where id = ${id}`).then((data)=>{
            if(data[0]==undefined){ //테이블상에 해당 id가 없다면
                console.log("존재 하지 않는 id입니다.")
                res.send("존재 하지 않는 id입니다.")

            }
            else{
                console.log(data[0])
                res.send(data[0]); //쿼리에서 받아온 해당 기관 정보 객체 전달
            }
        }).catch((err)=>{
            console.log(err)
            res.send(err) 
        })
        conn.release(); //쿼리 객체 release
    }).catch((err)=>{
        console.log(err)
        res.send(err)
    })
 
}
module.exports.inst_insert = (req,res)=>{
    let inst_id = req.body.id;//기관 id
    let inst_name = req.body.name;//기관이름
    let inst_phone = req.body.phone;//기관 휴대폰번호
    let rep_name = req.body.rep_name; //대표자 이름
    let rep_phone = req.body.rep_phone; //대표자 휴대폰번호
    let rep_email = req.body.rep_email; //대표자 이메일
    let rep_password = req.body.rep_password; //대표자 비밀번호
    let business_num = req.body.business_num; //사업자 번호
    if(!e_mail_check(rep_email))
    {
        res.status(400).send({'key':1}) //이메일 에러
        console.log("errorasdfasdf")
    }
    if(!phone_check(inst_phone))
    {
        res.status(400).send({'key':2})//기관번호 에러
    }
    if(!phone_check(rep_phone))
    {
        res.status(400).send({'key':3})//대표자번호에러
    }
    if(!company_check(business_num))
    {
        res.status(400).send({'key':4}) //사업자번호에러
    }
    
    pool.getConnection().then((conn)=>{
        conn.query(`insert into institution (institution_id,inst_name,inst_phone,activation,rep_name,rep_phone,rep_email,rep_password,business_num)values(${inst_id},'${inst_name}','${inst_phone}','N','${rep_name}','${rep_phone}','${rep_email}','${rep_password}','${business_num}')`).then((data)=>{
        }).catch((err)=>{
            console.log(err);
        })
        conn.release();
    }).catch((err)=>{
        console.log(err.code);
    })


    
}

module.exports.inst_modify = (req,res)=>{
    let person_id = req.body.id; //아이디
    let name = req.body.name; //담당자이름
    let phone_number = req.body.phone_number; //휴대폰번호
    let e_mail = req.body.e_mail; //이메일정보
    let pw = req.body.pw;
    let company_number = req.body.company_number;
   //이메일 정규식

   
    let e_mail_flag = false;
    let phone_flag = false;
    let company_flag = false;
    if(e_mail!=undefined)
    {
        if(!e_mail_check.test(e_mail))
        {
            console.log("이메일 양식 에러");
            res.status(400).send({'key':1}) //이메일양식에러
            
        }
    
        else{
            e_mail_flag=true;
        }
    }
    if(company_number!=undefined)
    {
        if(!company_number_check.test(company_number))
        {
            console.log("사업자 번호 양식 에러");
            res.status(400).send({'key':2}) //사업자 번호 양식 에러
            
        }
        else{
            company_flag=true;
        }
    }
    if(phone_number!=undefined)
    {
        if(!phone_number_check_1.test(phone_number)&&!phone_number_check_2.test(phone_number)) //일반전화번호와 휴대폰번호 둘다 양식에서 어긋날경우
        {
            console.log("번호 양식 에러");
            res.status(400).send({'key':3})
        }
        else{
            phone_flag=true;
        }
    }
    if(phone_flag==true && company_flag==true&&e_mail_flag==true)
    {
        pool.getConnection().then((conn)=>{
            conn.query()
        })
    }
    
    
}


