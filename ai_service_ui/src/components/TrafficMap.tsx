import { useEffect, useRef, useState } from 'react';
import 'mapbox-gl/dist/mapbox-gl.css';
import '../index.css';
import axios from "axios";

interface TrafficMapProps {
  onRouteSelect?: (route: { name: string; segment: string; direction: string }) => void;
}

declare global {
  interface Window {
    kakao: any;
  }
}

export const TrafficMap = ({ busStopInfo }) => {
  const mapRef = useRef<any>(null);
  const containerRef = useRef<HTMLDivElement>(null);
  const [map, setMap] = useState<any>(null);

  const kakaoKey = import.meta.env.VITE_KAKAO_KEY;

  useEffect(() => {

    const script = document.createElement("script");
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${kakaoKey}`;
    script.onload = () => {
      window.kakao.maps.load(() => {
        const center = new window.kakao.maps.LatLng(35.1788, 126.9119);
        const mapInstance = new window.kakao.maps.Map(containerRef.current, {
          center,
          level: 4,
        })
        mapRef.current = mapInstance;
        setMap(mapInstance);
         });
    };
    document.head.appendChild(script);
  }, []);

  async function getRouteStations() {
    const res = await axios.get("http://localhost:8080/api/station/9");
    return res.data.BUSSTOP_LIST;
  }

  // 3. stationId → 좌표 조회
  async function getStationLocation(st) {
    return {
      name: st.BUSSTOP_NAME,
      lat: st.LATITUDE,
      lng: st.LONGITUDE,
      busStopId: st.BUSSTOP_ID,
    };
  }

  function addMarker({ lat, lng, name, busStopId}: any) {
    const pos = new window.kakao.maps.LatLng(lat, lng);

    
    const overlayContent = document.createElement("div");
    overlayContent.className = "station-overlay";
    overlayContent.innerText = name;


    const overlay = new window.kakao.maps.CustomOverlay({
      position: pos,
      content: overlayContent,
      yAnchor: 2.0,
    });

    const marker = new window.kakao.maps.Marker({
      position: pos,
      map,
    });

    window.kakao.maps.event.addListener(marker, "mouseover", () => {
      overlay.setMap(map);
    });

    window.kakao.maps.event.addListener(marker, "mouseout", () => {
      overlay.setMap(null);
    });

    window.kakao.maps.event.addListener(marker, "click", () => {
      fetchWeather(lat, lng, name, busStopId);   
    });

    return marker;
  }

  const fetchWeather = async (lat, lng, name, busStopId) => {
    
    const data = {
      lat: lat,
      lng: lng,
      name: name,
      busStopId: busStopId,
    }
    busStopInfo(data);  // ⭐ 부모로 전달
  };

  async function loadRoute() {
    if (!map) return;

    const stations = await getRouteStations();
    
    for (const st of stations) {
      const loc = await getStationLocation(st);
      addMarker(loc);
      //TODO: 클릭시 위도 경도를 기준으로 기상 데이터 가져오기
    }
    
  }

  useEffect(() => {
    if (map) loadRoute();
  }, [map]);

  return (
    <div
      ref={containerRef}
      style={{ width: "100%", height: "600px", borderRadius: "8px" }}
    />
  );
};
