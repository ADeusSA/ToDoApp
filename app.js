const express = require('express')
const app = express()
const mongoClient = require('mongodb').MongoClient

const url = "mongodb://localhost:27017"

app.use(express.json())

mongoClient.connect(url, (err, db) => {

    if (err) {
        console.log("Error while connecting mongo client")
    } else {

        const todoDb = db.db('todoDb')

        /*
        Работа с пользователями
        */
        const collectionUsers = todoDb.collection('Users')

        //Регистрация
        app.post('/signup', (req, res) => {

            const newUser = {
                username: req.body.username,
                email: req.body.email,
                password: req.body.password
            }

            const query = { email: newUser.email }

            collectionUsers.findOne(query, (err, result) => {

                if (result == null) {

                    collectionUsers.insertOne(newUser, (err, result) => {
                        const objToSend = {
                        id: result._id,
                        username: result.username,
                        email: result.email
                        }

                        let jjs = JSON.stringify(objToSend)
                        console.log('count: %s', jjs);

                        res.status(200).send(JSON.stringify(objToSend))
                    })
                } else {
                    res.status(400).send()
                }

            })

        })

        //Авторизация
        app.post('/login', (req, res) => {

            const query = {
                email: req.body.email, 
                password: req.body.password
            }

            collectionUsers.findOne(query, (err, result) => {

                if (result != null) {

                    const objToSend = {
                        id: result._id,
                        username: result.username,
                        email: result.email
                    }

                    // let jjs = JSON.stringify(objToSend)
                    // console.log('count: %s', jjs);

                    res.status(200).send(JSON.stringify(objToSend))

                } else {
                    res.status(404).send()
                }

            })

        })

        //Смена/Восстановление пароля
        app.post('/forget', (req, res) =>{

            const query = {
                username: req.body.username,
                email: req.body.email
            }

            collectionUsers.findOne(query, (err, result) => {

                if (result != null) {
                    collectionUsers.updateOne({"email": req.body.email}, 
                        {$set: {"password": req.body.password} }, (err, result) =>{
                        res.status(200).send()
                    })
                } else {
                    res.status(404).send()
                }
            })
        })

        /*
        Работа с задачами
        */



    }

})

app.listen(3000, () => {
    console.log("Listening on port 3000...")
})
