package com.example.scooterrentalsystem.service;

import com.example.scooterrentalsystem.dao.RentalPointDao;
import com.example.scooterrentalsystem.dao.ScooterDao;
import com.example.scooterrentalsystem.entity.RentalPoint;
import com.example.scooterrentalsystem.entity.Scooter;
import com.example.scooterrentalsystem.exeption.ConflictException;
import com.example.scooterrentalsystem.exeption.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class RentalPointService {
    private final RentalPointDao rentalPointDao;
    private final ScooterDao scooterDao;

    public RentalPointService(RentalPointDao rentalPointDao, ScooterDao scooterDao) {
        this.rentalPointDao = rentalPointDao;
        this.scooterDao = scooterDao;
    }
    public RentalPoint addRentalPoint(String name, BigDecimal latitude, BigDecimal longitude, Long parentId){
        RentalPoint parent = null;
        if (parentId!=null){
            parent = rentalPointDao.findById(parentId).orElseThrow(()->new ResourceNotFoundException("Родительская точка с ID " + parentId + " не найдена"));
        }
        RentalPoint newPoint = new RentalPoint(name,latitude,longitude,parent);
        rentalPointDao.save(newPoint);
        return newPoint;
    }
    public void updateRentalPoint(Long id,String name, BigDecimal latitude, BigDecimal longitude){
        RentalPoint point = rentalPointDao.findById(id).orElseThrow(()-> new ResourceNotFoundException("Точка проката не найдена"));
        point.setName(name);
        point.setLatitude(latitude);
        point.setLongitude(longitude);
    }
    public void deleteRentalPoint(Long id){
        RentalPoint point = rentalPointDao.findById(id).orElseThrow(()-> new ResourceNotFoundException("Точка проката не найдена"));
        List<Scooter> scootersHere = scooterDao.findAvailableByRentalPoint(id);
        if (!scootersHere.isEmpty()){
            throw new ConflictException("Невозможно удалить точку проката: на ней еще стоят самокаты!");
        }
        rentalPointDao.delete(point);
    }
    @Transactional(readOnly = true)
    public List<RentalPoint> getRootPoints() {
        return rentalPointDao.findRootPoints();
    }
    @Transactional(readOnly = true)
    public List<RentalPoint> getSubPoints(Long parentId){
        return rentalPointDao.findByParentId(parentId);
    }
    @Transactional(readOnly = true)
    public List<Scooter> getAvailableScootersAtPoint(Long pointId){
        rentalPointDao.findById(pointId).orElseThrow(()->new ResourceNotFoundException("Точка проката не найдена"));
        return scooterDao.findAvailableByRentalPoint(pointId);
    }
}
