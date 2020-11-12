const crypto = require('crypto');
// crypto.randomBytes(64,(err,buffer)=>{
//     let password ='123'
//     crypto.pbkdf2(password, buffer.toString('base64'), 130495, 64, 'sha512', (err, hashed)=> {
//         if(err){
//         console.log(err);
//         } else{
//         hashed_password=hashed.toString('base64');
//         salt=buffer.toString('base64')
//         console.log(hashed_password);
//         console.log(salt)
//         }
//     });
// })
let buffer= 'vvHWS5So4IM5kMulM3pf/6bwtGk0hL2/HVOK6Oku/aSm7QzaIkEZUevWys/UI1KD6p9NOmjHmUcuNLi0Y5p7wg=='
let password='123'
crypto.pbkdf2(password, buffer.toString('base64'), 130495, 64, 'sha512', (err, hashed)=> {
    if(err){
    console.log(err);
    } else{
    hashed_password=hashed.toString('base64');
    //salt=buffer.toString('base64')
    console.log(hashed_password);
    //console.log(salt)
    }
});