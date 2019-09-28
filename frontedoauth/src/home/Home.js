import React, { Component } from 'react';
import './Home.css';
import Carousel from 'react-bootstrap/Carousel'
import "bootstrap/dist/css/bootstrap.css";
import mainPic from "../img/FIRST-PHOTO-CARS.jpg"
import mainPic2 from "../img/slider2.jpg"
import mainPic3 from "../img/Wethersfield-Car-Show.jpg"
import mainPic4 from "../img/fiat-500-furniture-5.jpg"
import mainPic5 from "../img/carshow2014.jpg"
import mainPic6 from "../img/FI-volkswagen-beetle-2019.jpg"
import mainPic7 from "../img/asphalt-auto-automobile-1280560.jpg"
import mainPic8 from "../img/309491.jpg"
import mainPic9 from "../img/ford-chevrolet-retro-cars.jpg"

class Home extends Component {
    render() {
        return (
            <div className="home-container">
                <div className="container">
                    <Carousel>
                        <Carousel.Item>
                            <img
                                className="d-block w-100"
                                src={mainPic}
                                alt="pic"
                            />
                        </Carousel.Item>

                        <Carousel.Item>
                        <img
                            className="d-block w-100"
                            src={mainPic3}
                            alt="pic"
                        />
                    </Carousel.Item>

                        <Carousel.Item>
                            <img
                                className="d-block w-100"
                                src={mainPic5}
                                alt="pic"
                            />
                        </Carousel.Item>
                        <Carousel.Item>
                            <img
                                className="d-block w-100"
                                src={mainPic6}
                                alt="pic"
                            />
                        </Carousel.Item>
                        <Carousel.Item>
                            <img
                                className="d-block w-100"
                                src={mainPic7}
                                alt="pic"
                            />
                        </Carousel.Item>
                        <Carousel.Item>
                            <img
                                className="d-block w-100"
                                src={mainPic8}
                                alt="pic"
                            />
                        </Carousel.Item>
                        <Carousel.Item>
                            <img
                                className="d-block w-100"
                                src={mainPic9}
                                alt="pic"
                            />
                        </Carousel.Item>
                    </Carousel>
                </div>
            </div>
        )
    }
}

export default Home;